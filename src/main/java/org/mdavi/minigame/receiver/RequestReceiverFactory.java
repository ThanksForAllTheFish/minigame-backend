package org.mdavi.minigame.receiver;

import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings(value = {"restriction"})
public interface RequestReceiverFactory
{

  RequestReceiver getRequestReceiver (HttpExchange request);

}
