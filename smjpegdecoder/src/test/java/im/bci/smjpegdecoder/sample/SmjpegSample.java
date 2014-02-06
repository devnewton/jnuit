package im.bci.smjpegdecoder.sample;

import im.bci.smjpegdecoder.SmjpegDecoder;
import im.bci.smjpegdecoder.SmjpegOutputBuffers;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.imageio.ImageIO;

/**
 * Extract jpeg frames from smjpeg video.
 * @author devnewton
 */
public class SmjpegSample {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String testDir = System.getProperty("user.home") + "/tmp/smjpeg";
        try (RandomAccessFile file = new RandomAccessFile(testDir + "/test.smjpeg", "r")) {
            SmjpegDecoder parser = new SmjpegDecoder(file);
            BufferedImage image = new BufferedImage(parser.getVideoWidth(), parser.getVideoHeight(), BufferedImage.TYPE_INT_RGB);
            int frame = 0;
            SmjpegOutputBuffers outputBuffers = new SmjpegOutputBuffers(parser);
            final int timePerFrame = 1000 / 60;
            int lastDecodedFrameTimeStamp = -1;
            for (int timeElapsed = 0; timeElapsed < parser.getClipLengthInMilliseconds(); timeElapsed += timePerFrame) {
                parser.getFrame(timeElapsed, outputBuffers);
                if (lastDecodedFrameTimeStamp != outputBuffers.getVideoFrameTimestamp()) {
                    int index = 0;
                    for (int y = 0; y < parser.getVideoHeight(); ++y) {
                        for (int x = 0; x < parser.getVideoWidth(); ++x) {
                            int r = outputBuffers.getVideoFrame().get(index++) & 0xff;
                            int g = outputBuffers.getVideoFrame().get(index++) & 0xff;
                            int b = outputBuffers.getVideoFrame().get(index++) & 0xff;
                            int a = outputBuffers.getVideoFrame().get(index++) & 0xff;
                            Color c = new Color(r, g, b, a);
                            image.setRGB(x, y, c.getRGB());
                        }
                    }
                    ImageIO.write(image, "png", new File(String.format("%s/frames/%06d.png", testDir, frame++)));
                    lastDecodedFrameTimeStamp = outputBuffers.getVideoFrameTimestamp();
                }
            }
        }
    }
}
