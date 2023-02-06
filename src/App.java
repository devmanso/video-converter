import org.bytedeco.ffmpeg.avutil.LogCallback;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.librealsense.frame;
import org.bytedeco.opencv.opencv_core.IplImage;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public class App {
    public static void main(String[] args) throws Exception {
        //TODO: get original video file and conversion by user as input
        String video = "video0.mov";
        String gif = "video.gif";
        
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(video);
        frameGrabber.setFormat("gif");
        frameGrabber.start();
        
        //frameGrabber.setFormat(gif);

        // video properties
        double audioBitrate = frameGrabber.getAudioBitrate();
        double videoBitrate = frameGrabber.getVideoBitrate();
        double framerate = frameGrabber.getFrameRate();
        double aspectRatio = frameGrabber.getAspectRatio();
        int frameCount = frameGrabber.getLengthInFrames();
        
        // JavaCV has no built-in way to encode a sequence of frames into a .gif
        // so we must use BufferedImage from java.awt.image instead.
        BufferedImage[] images = new BufferedImage[frameCount];

        for (int i = 0; i < frameCount; i++) {
            Frame frame = frameGrabber.grab();
            Java2DFrameConverter frameConverter = new Java2DFrameConverter();
            images[i] = frameConverter.getBufferedImage(frame);
        }
        frameGrabber.stop();
        OutputStream outputStream = new FileOutputStream(gif);
        ImageIO.write(images[0], "gif", outputStream);
        outputStream.close();
        
    }
}
