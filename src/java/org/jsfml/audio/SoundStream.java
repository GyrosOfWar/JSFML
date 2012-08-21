package org.jsfml.audio;

import org.jsfml.Intercom;
import org.jsfml.NotNull;
import org.jsfml.system.Time;

/**
 * Abstract base class for streamed audio sources.
 */
@Intercom
public abstract class SoundStream extends SoundSource {
    /**
     * Represents a chunk of audio data provided by a {@link SoundStream} when
     * new data is requested.
     */
    @Intercom
    public static class Chunk {
        @Intercom
        private final short[] data;

        @Intercom
        private boolean last = false;

        /**
         * Constructs a new chunk containing the given data.
         *
         * @param data An array of 16-bit samples representing the chunk's audio data.
         */
        public Chunk(@NotNull short[] data) {
            if (data == null)
                throw new NullPointerException("data must not be null.");

            this.data = data;
        }

        /**
         * Gets the audio sample data in this chunk.
         *
         * @return The audio sample data in this chunk.
         */
        public short[] getData() {
            return data;
        }

        /**
         * Tests whether this chunk is the last chunk of data in the stream.
         *
         * @return <tt>true</tt> if the chunk is marked as the last one, <tt>false</tt>
         *         otherwise
         * @see #setLast(boolean)
         */
        public boolean isLast() {
            return last;
        }

        /**
         * Determines whether this chunk is the last chunk of data in the stream.
         * <p/>
         * If set to <tt>true</tt>, the {@link SoundStream} will stop playing after
         * the audio samples contained in this chunk have been played.
         * <p/>
         * This property is set to <tt>false</tt> by default.
         *
         * @param last <tt>true</tt> to flag this chunk as the last chunk in the stream,
         *             <tt>false</tt> otherwise.
         */
        public void setLast(boolean last) {
            this.last = last;
        }
    }

    /**
     * Default constructor.
     */
    public SoundStream() {
        super();
    }

    @Override
    protected native long nativeCreate();

    @Override
    protected native void nativeDelete();

    /**
     * Starts playing the stream or resumes it if it is currently paused.
     */
    public native void play();

    /**
     * Pauses playback of the stream if it is currently playing.
     */
    public native void pause();

    /**
     * Stops playing the stream.
     */
    public native void stop();

    /**
     * Gets the amount of audio channels of this stream.
     *
     * @return The amount of audio channels of this stream.
     */
    public native int getChannelCount();

    /**
     * Gets the sample rate of this stream.
     *
     * @return The sample rate of this stream in samples per second.
     */
    public native int getSampleRate();

    private native void nativeSetPlayingOffset(Time offset);

    /**
     * Sets the playing offset at which to play from the stream.
     *
     * @param offset The playing offset, in milliseconds, at which to play from the stream.
     */
    public final void setPlayingOffset(@NotNull Time offset) {
        if (offset == null)
            throw new NullPointerException("offset must not be null.");

        nativeSetPlayingOffset(offset);
    }

    /**
     * Gets the playing offset at which to play from the stream.
     *
     * @return The playing offset, in milliseconds, at which to play from the stream.
     */
    public native Time getPlayingOffset();

    /**
     * Enables or disables repeated looping of the sound stream playback.
     *
     * @param loop <tt>true</tt> to enable looping, <tt>false</tt> to disable.
     */
    public native void setLoop(boolean loop);

    /**
     * Returns whether or not the sound stream playback is looping.
     *
     * @return <tt>true</tt> if it is looping, <tt>false</tt> if not.
     */
    public native boolean isLoop();

    @Override
    native int nativeGetStatus();

    /**
     * Defines the audio stream parameters.
     * <p/>
     * Before the stream can be played, the implementing class must call this method.
     *
     * @param channelCount The amount of audio channels (e.g. 1 for mono, 2 for stereo).
     * @param sampleRate   The sample rate in samples per second.
     */
    protected native void initialize(int channelCount, int sampleRate);

    /**
     * Requests a new chunk of audio data.
     * <p/>
     * This method is called when the audio stream has played all buffered samples and needs
     * new samples to continue playing.
     *
     * @return The next chunk of audio data. If the chunk is marked as the last chunk (see
     *         {@link Chunk#setLast(boolean)}, the stream will stop playing after playing this chunk.
     *         To stop playback immediately, <tt>null</tt> may be returned as well.
     * @see Chunk
     */
    @Intercom
    protected abstract Chunk onGetData();

    /**
     * Re-positions the stream's current playing offset.
     * <p/>
     * This method is called when the stream is reset or a re-positioning has been requested
     * via {@link #setPlayingOffset(org.jsfml.system.Time)}.
     *
     * @param time The time offset to jump to.
     */
    @Intercom
    protected abstract void onSeek(Time time);
}
