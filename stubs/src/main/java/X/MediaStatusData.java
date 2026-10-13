package X;

/**
 * A status made of media (photo/video).
 * Exposes the media info so the status downloader can save it.
 * 
 * Real class: {@code LX/LdR} (interface)
 * Real return type for getMediaInfo: {@code LX/6lS}
 */
public interface MediaStatusData extends StatusData { // LX/LdR

    FMessageMedia getMediaInfo(); // AgX()LX/6lS
}
