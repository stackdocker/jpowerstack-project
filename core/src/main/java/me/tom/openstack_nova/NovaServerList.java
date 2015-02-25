package me.tom.openstack_nova;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import com.google.inject.Module;
import org.jclouds.ContextBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

public class NovaServerList implements Closeable {
    private final NovaApi novaApi;
    private final Set<String> zones;

    public static void main(String[] args) throws IOException {
    	NovaServerList jcloudsNova = new NovaServerList();

        try {
            jcloudsNova.listServers();
            jcloudsNova.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jcloudsNova.close();
        }
    }

    public NovaServerList() {
        Iterable<Module> modules = ImmutableSet.<Module>of(new SLF4JLoggingModule());

        String provider = "openstack-nova";
        String identity = "demo:demo"; // tenantName:userName
        String credential = "devstack";

        novaApi = ContextBuilder.newBuilder(provider)
                .endpoint("http://xxx.xxx.xxx.xxx:5000/v2.0/")
                .credentials(identity, credential)
                .modules(modules)
                .buildApi(NovaApi.class);
        zones = novaApi.getConfiguredZones();
    }

    private void listServers() {
        for (String zone : zones) {
            ServerApi serverApi = novaApi.getServerApiForZone(zone);

            System.out.println("Servers in " + zone);

            for (Server server : serverApi.listInDetail().concat()) {
                System.out.println("  " + server);
            }
        }
    }
    
	public void close() throws IOException {
		// TODO Auto-generated method stub
		Closeables.close(novaApi, true);
	}

}
