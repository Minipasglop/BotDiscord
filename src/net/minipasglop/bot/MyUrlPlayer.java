package net.minipasglop.bot;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.audio.player.URLPlayer;
import net.dv8tion.jda.utils.SimpleLog;
import org.apache.http.HttpHost;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * Created by Minipasglop on 20/09/2016.
 */
public class MyUrlPlayer extends URLPlayer {
    protected String userAgent;
    protected URL urlOfResource;
    protected InputStream resourceStream;
    protected BufferedInputStream bufferedResourceStream;
    protected boolean started;
    protected boolean playing;
    protected boolean paused;
    protected boolean stopped;

    public MyUrlPlayer(JDA api) throws MalformedURLException {
        super(api);
    }

    public void setAudioUrl(URL urlOfResource, int bufferSize) throws IOException, UnsupportedAudioFileException {
        if(urlOfResource == null) {
            throw new IllegalArgumentException("A null URL was provided to the Player! Cannot find resource to play from a null URL!");
        } else {
            this.urlOfResource = urlOfResource;
            URLConnection conn = null;
            HttpHost jdaProxy = this.api.getGlobalProxy();
            if(jdaProxy != null) {
                InetSocketAddress proxyAddress = new InetSocketAddress(jdaProxy.getHostName(), jdaProxy.getPort());
                Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
                conn = urlOfResource.openConnection(proxy);
            } else {
                conn = urlOfResource.openConnection();
            }

            if(conn == null) {
                throw new IllegalArgumentException("The provided URL resulted in a null URLConnection! Does the resource exist?");
            } else {
                conn.setRequestProperty("user-agent", this.userAgent);
                this.resourceStream = conn.getInputStream();
                this.bufferedResourceStream = new BufferedInputStream(this.resourceStream, bufferSize);
                this.setAudioSource(AudioSystem.getAudioInputStream(this.bufferedResourceStream));
            }
        }
    }

    public void play() {
        if(this.started && this.stopped) {
            throw new IllegalStateException("Cannot start a player after it has been stopped.\nPlease use the restart method or load a new file.");
        } else {
            this.started = true;
            this.playing = true;
            this.paused = false;
            this.stopped = false;
        }
    }

    public void pause() {
        this.playing = false;
        this.paused = true;
    }

    public void stop() {
        this.playing = false;
        this.paused = false;
        this.stopped = true;

        try {
            this.bufferedResourceStream.close();
            this.resourceStream.close();
        } catch (IOException var2) {
            SimpleLog.getLog("JDAPlayer").fatal("Attempted to close the URLPlayer resource stream during stop() cleanup, but hit an IOException");
            SimpleLog.getLog("JDAPlayer").log(var2);
        }

    }

    public void restart() {
        URL oldUrl = this.urlOfResource;

        try {
            this.bufferedResourceStream.close();
            this.resourceStream.close();
            this.reset();
            this.setAudioUrl(oldUrl);
            this.play();
        } catch (IOException var3) {
            SimpleLog.getLog("JDAPlayer").fatal("Attempted to restart the URLStream playback, but something went wrong!");
            SimpleLog.getLog("JDAPlayer").log(var3);
        } catch (UnsupportedAudioFileException var4) {
            SimpleLog.getLog("JDAPlayer").log(var4);
        }

    }

    public boolean isStarted() {
        return this.started;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public void reset() {
        this.started = false;
        this.playing = false;
        this.paused = false;
        this.stopped = true;
        this.urlOfResource = null;
        this.resourceStream = null;
    }

}
