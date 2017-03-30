package com.aerokube.selenoid.misc;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.net.UnknownHostException;

public class JettyRule extends ExternalResource {

    private static final Logger LOG = LoggerFactory.getLogger(JettyRule.class); 
    
    private Server server;
    
    private static final String CONTEXT_PATH = "/";
    private static final String CLASS_PATH = "target/classes";
    
    private int port;
    
    @Override
    protected void before() throws Throwable {
        port = findAvailablePort();
        LOG.info(String.format("Starting Jetty on port %d", port));
        server = new Server();
        server.setConnectors(new Connector[]{
                createConnector(port)
        });
        WebAppContext context = new WebAppContext() {
            {
                setServer(server);
                setContextPath(CONTEXT_PATH);
                URL webappResource = getClass().getClassLoader().getResource("webapp");
                if (webappResource == null) {
                    throw new IllegalStateException("Webapp resource not found!");
                }
                setWar(webappResource.toURI().toString());
            }
        };
        WebAppClassLoader classLoader = new WebAppClassLoader(getClass().getClassLoader(), context);
        classLoader.addClassPath(CLASS_PATH);
        context.setClassLoader(classLoader);
        server.setHandler(context);
        server.start();
    }

    private ServerConnector createConnector(int port) {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        connector.setHost("0.0.0.0");
        return connector;
    }

    private static int findAvailablePort() {
        final int MIN_PORT = 1024;
        final int MAX_PORT = 65535;
        for (int i = MIN_PORT; i <= MAX_PORT; i++) {
            try {
                int port = new java.util.Random().nextInt((MAX_PORT - MIN_PORT) + 1) + MIN_PORT;
                ServerSocket socket = new ServerSocket(port);
                socket.close();
                return port;
            } catch (IOException ignored) {
            }
        }
        throw new IllegalStateException("Can't find a free port");
    }

    @Override
    protected void after() {
        try {
            LOG.info("Stopping Jetty server");
            server.stop();
        } catch (Exception e) {
            LOG.error("Failed to stop Jetty");
        }
    }
    
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOG.error("Failed to determine host name");
            return "localhost";
        }
    }
    
    int getPort() {
        return port;
    }
    
}
