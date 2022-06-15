package de.php_perfect.intellij.ddev.php.server;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.jetbrains.php.config.servers.PhpServer;
import com.jetbrains.php.config.servers.PhpServersWorkspaceStateComponent;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

final class ServerConfigManagerImplTest extends BasePlatformTestCase {

    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void configure() throws MalformedURLException {
        final ServerConfigManager serverConfigManager = ServerConfigManager.getInstance(this.getProject());

        final ServerConfig serverConfig = new ServerConfig(
                "C:\\Users\\test\\AppData\\Local\\Temp\\my-project",
                "/var/www/html",
                new URL("https://test.ddev.site")
        );

        serverConfigManager.configure(serverConfig);
        // Check server gets replaced
        serverConfigManager.configure(serverConfig);

        assertServerConfigMatches(serverConfig);
    }

    private void assertServerConfigMatches(ServerConfig serverConfig) {
        final List<PhpServer> servers = PhpServersWorkspaceStateComponent.getInstance(this.getProject()).getServers();

        Assert.assertEquals(1, servers.size());

        final PhpServer server = servers.get(0);
        Assert.assertEquals("DDEV", server.getName());
        Assert.assertEquals("test.ddev.site", server.getHost());

        var mappings = server.getMappings();

        Assert.assertEquals(1, mappings.size());

        var mapping = mappings.get(0);

        Assert.assertEquals(serverConfig.getLocalPath(), mapping.getLocalRoot());
        Assert.assertEquals(serverConfig.getRemotePath(), mapping.getRemoteRoot());
    }
}
