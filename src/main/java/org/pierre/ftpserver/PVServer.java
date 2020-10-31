package org.pierre.ftpserver;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PVServer {
    public static void main(String[] args) {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(1234);// set the port of the listener (choose your desired port, not 1234)
        serverFactory.addListener("default", factory.createListener());
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File("myusers.properties"));//choose any. We're telling the FTP-server where to read its user list
        userManagerFactory.setPasswordEncryptor(new PasswordEncryptor()
        {//We store clear-text passwords in this example


            public String encrypt(String password) {
                return password;
            }


            public boolean matches(String passwordToCheck, String storedPassword) {
                return passwordToCheck.equals(storedPassword);
            }
        });
        //Let's add a user, since our myusers.properties file is empty on our first test run
        BaseUser user = new BaseUser();
        user.setName("test");
        user.setPassword("test");
        user.setHomeDirectory("D:\\pierre");
        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(new WritePermission());
        user.setAuthorities(authorities);
        UserManager um = userManagerFactory.createUserManager();
        try
        {
            um.save(user);//Save the user to the user list on the filesystem
        }
        catch (FtpException e1)
        {
            System.out.println(e1);
        }
        serverFactory.setUserManager(um);
        Map<String, Ftplet> m = new HashMap<String, Ftplet>();
        m.put("miaFtplet", new Ftplet()
        {
            public void init(FtpletContext ftpletContext) throws FtpException {
                //System.out.println("init");
                //System.out.println("Thread #" + Thread.currentThread().getId());
            }
            public void destroy() {
                //System.out.println("destroy");
                //System.out.println("Thread #" + Thread.currentThread().getId());
            }
            public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException
            {
                //System.out.println("beforeCommand " + session.getUserArgument() + " : " + session.toString() + " | " + request.getArgument() + " : " + request.getCommand() + " : " + request.getRequestLine());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }

            public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException
            {
                //System.out.println("afterCommand " + session.getUserArgument() + " : " + session.toString() + " | " + request.getArgument() + " : " + request.getCommand() + " : " + request.getRequestLine() + " | " + reply.getMessage() + " : " + reply.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }

            public FtpletResult onConnect(FtpSession session) throws FtpException, IOException
            {
                //System.out.println("onConnect " + session.getUserArgument() + " : " + session.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }


            public FtpletResult onDisconnect(FtpSession session) throws FtpException, IOException
            {
                //System.out.println("onDisconnect " + session.getUserArgument() + " : " + session.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }
        });
        serverFactory.setFtplets(m);
        //Map<String, Ftplet> mappa = serverFactory.getFtplets();
        //System.out.println(mappa.size());
        //System.out.println("Thread #" + Thread.currentThread().getId());
        //System.out.println(mappa.toString());
        FtpServer server = serverFactory.createServer();
        try
        {
            server.start();//Your FTP server starts listening for incoming FTP-connections, using the configuration options previously set
        }
        catch (FtpException ex)
        {
            //Deal with exception as you need
        }
    }
}
