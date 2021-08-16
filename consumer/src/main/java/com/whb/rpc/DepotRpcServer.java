package com.whb.rpc;

import com.whb.service.IDepositService;
import com.whb.vo.GoodTransferVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Component
public class DepotRpcServer implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(DepotRpcServer.class);
    private int port = 8880;

    private ApplicationContext context ;

    private ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private class ServerService implements Runnable{
        private ServerSocket serverSocket;
        private ApplicationContext context;
        public ServerService(ApplicationContext context){
            this.context = context;
        }
        @Override
        public void run() {
            try {
                logger.info("启动服务端……");
                serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(port));
                while(true){
                    Socket socket = serverSocket.accept();
                    service.execute(new ServerTask(socket,this.context));
                    logger.info("--------------Rpc server is running on port" + port+"------------------");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ServerTask implements Runnable{

        private Socket acceptClient = null;
        private ApplicationContext appContext;
        public ServerTask(Socket socket,ApplicationContext context){
            this.acceptClient = socket;
            this.appContext = context;
        }

        @Override
        public void run() {

            try(ObjectInputStream inputStream =
                        new ObjectInputStream(acceptClient.getInputStream());
                ObjectOutputStream outputStream =
                        new ObjectOutputStream(acceptClient.getOutputStream())){

                try {
                    String serviceName = inputStream.readUTF();
                    String methodName = inputStream.readUTF();
                    Class<?>[] parmTypes =  (Class<?>[]) inputStream.readObject();
                    Object[] args = (Object[])inputStream.readObject();

                    IDepositService depotServiceImpl = appContext.getBean(IDepositService.class);
                    GoodTransferVo goodTransferVo = (GoodTransferVo)args[0];
                    ;
//                    throw new RuntimeException("库存系统异常了！！！！！");
                    outputStream.writeUTF(depotServiceImpl.Inventory(goodTransferVo)+"");

                } catch (Exception e) {
                    e.printStackTrace();
                    outputStream.writeUTF(RpcConst.EXCEPTION);
                }


            }catch(Exception e){
                e.printStackTrace();
            }finally {
                try {
                    acceptClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @PostConstruct
    public void startService(){
        Thread thread =new Thread(new ServerService(context));
        thread.start();
    }
}
