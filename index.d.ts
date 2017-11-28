   export interface InstantMessage{
    initWebClient(socketUrl,chatSecret):Promise<string>
  
    disConnectWebClient():void;
  
    connectWebClient():void;
  
    sendMessage(message):Promise<String>;
  
    attachInstantMessageListener(listener):EmitterSubscription;
  }
  export const InstantMessage: InstantMessage;