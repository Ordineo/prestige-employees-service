package be.ordina.ordineo.importusers;

import be.ordina.ordineo.service.GitHubService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;

/**
 * @author Maarten Casteels
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class InitUsersImportTest {

    @Mock
    private GitHubService gitHubService;

    @InjectMocks
    private InitUsersImport importer;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        Mockito.verifyNoMoreInteractions(gitHubService);
    }

    @Test
    public void shouldCallGitHubServiceWhenEventIsTriggered() throws Exception {
        final ApplicationReadyEvent event = Mockito.mock(ApplicationReadyEvent.class);

        importer.onApplicationEvent(event);

        Mockito.verify(gitHubService).importUsers();
    }
}