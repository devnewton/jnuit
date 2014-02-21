/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package im.bci.jnuit.lwjgl.audio;

import com.infinityk.simplesoundengine.SimpleSoundEngine;
import im.bci.jnuit.NuitAudio;
import im.bci.jnuit.audio.Sound;
import im.bci.jnuit.lwjgl.assets.VirtualFileSystem;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devnewton
 */
public class OpenALNuitAudio implements NuitAudio {

    private static final Logger logger = Logger.getLogger(OpenALNuitAudio.class.getName());
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private final VirtualFileSystem vfs;
    private SimpleSoundEngine engine;
    private final ExecutorService executor;
    private Runnable poll;

    private abstract class AbstractOpenALTask implements Runnable {

        @Override
        public final void run() {
            try {
                doRun();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "OpenAL error", ex);
            }
        }

        protected abstract void doRun() throws Exception;
    }

    public OpenALNuitAudio(VirtualFileSystem virtualFileSystem) {
        this.poll = new AbstractOpenALTask() {
            
            @Override
            public void doRun() {
                engine.poll();
                Thread.yield();
                if (null != poll) {
                    executor.submit(poll);
                }
            }
        };
        this.vfs = virtualFileSystem;
        executor = Executors.newSingleThreadExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = Executors.defaultThreadFactory().newThread(runnable);
                thread.setDaemon(true);
                return thread;
            }
        });
        executor.submit(new AbstractOpenALTask() {

            @Override
            public void doRun() {
                engine = new SimpleSoundEngine(vfs);
                engine.init();
                executor.submit(poll);
            }
        });
    }

    public void close() {
        poll = null;
        executor.submit(new AbstractOpenALTask() {

            @Override
            public void doRun() {
                engine.destroy();
            }
        });
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(OpenALNuitAudio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearAll() {
        executor.submit(new AbstractOpenALTask() {

            @Override
            public void doRun() {
                engine.unloadUselessSounds();
            }
        });
    }

    public void clearUseless() {
        clearAll();
    }

    @Override
    public Sound getSound(final String name) {
        return new Sound() {

            @Override
            public void play() {
                if (soundEnabled) {
                    executor.submit(new AbstractOpenALTask() {

                        @Override
                        public void doRun() {
                            engine.playSound(name);
                        }
                    });
                }
            }

            @Override
            public void stop() {
            }
        };
    }

    @Override
    public void playMusicIfEnabled(final String name) {
        if (musicEnabled) {
            executor.submit(new AbstractOpenALTask() {

                @Override
                public void doRun() {
                    try {
                        engine.playMusic(name, true);
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, "Cannot play music " + name, ex);
                    }
                }
            });
        }
    }

    @Override
    public void stopMusic() {
        executor.submit(new AbstractOpenALTask() {

            @Override
            public void doRun() {
                engine.stopMusic();
            }
        });

    }

    @Override
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    @Override
    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    @Override
    public void setSoundEnabled(boolean e) {
        this.soundEnabled = e;
    }


    @Override
    public void setMusicEnabled(boolean e) {
        this.musicEnabled = e;
        if (!e) {
            stopMusic();
        }
    }
}
