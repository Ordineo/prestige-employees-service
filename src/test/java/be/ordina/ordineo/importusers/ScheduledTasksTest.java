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

/**
 * @author Maarten Casteels
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class ScheduledTasksTest {
    @Mock
    private GitHubService gitHubService;

    @InjectMocks
    private ScheduledTasks tasks;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        Mockito.verifyNoMoreInteractions(gitHubService);
    }

    @Test
    public void shouldCallGitHubServiceWhenEventIsTriggered() throws Exception {
        tasks.importGitHubUsers();

        Mockito.verify(gitHubService).importUsers();
    }

}