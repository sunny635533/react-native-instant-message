import {
  // NativeAppEventEmitter,
  DeviceEventEmitter,
  NativeEventEmitter,
  NativeModules,
  Platform,
} from 'react-native';

const { RNInstantMessage } = NativeModules;

const getInstantMessage = 'RNInstantMessage:getInstantMessage';

export class InstantMessageClass {
  
  initWebClient(socketUrl, chatSecret) {

    return RNInstantMessage.initWebClient(socketUrl, chatSecret);
  }

  disConnectWebClient() {
    RNInstantMessage.disConnectWebClient();
  }

  connectWebClient() {
    RNInstantMessage.connectWebClient();
  }

  sendMessage(message) {
    return RNInstantMessage.sendMessage(message);
  }

  attachInstantMessageListener(listener) {
    // return NativeAppEventEmitter.addListener(getInstantMessage, listener);
    if(Platform.OS === 'android'){
      return DeviceEventEmitter.addListener(getInstantMessage,listener);
    }else{
      return new NativeEventEmitter(RNInstantMessage).addListener(getInstantMessage,listener);
    }
    
  }

}
export const InstantMessage = new InstantMessageClass;

