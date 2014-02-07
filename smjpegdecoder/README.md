# smjpegdecoder

A pure Java [SMJPEG](http://wiki.multimedia.cx/index.php?title=SMJPEG) decoder.

SMJPEG is video [format](doc/SMJPEG.md) used by [Loki games](http://www.lokigames.com/development/smjpeg.php3).

Frames are individually compressed as jpeg images. Jpeg decompression is done with Matthias Mann's [jpegdecoder](http://hg.l33tlabs.org/JpegDecoder/).

# Documentation

- [SMJPEG file format specification](docs/SMJPEG.md)
- [Frequently asked questions](docs/FAQ.md)

# Sample code

[Extract smjpeg frames](src/test/java/im/bci/smjpegdecoder/sample/SmjpegSample.java)
