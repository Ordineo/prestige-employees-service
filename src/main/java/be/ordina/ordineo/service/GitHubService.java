package be.ordina.ordineo.service;

import java.io.IOException;

/**
 * @author Maarten Casteels
 * @since 2017
 */
public interface GitHubService {

    void importUsers() throws IOException;
}
