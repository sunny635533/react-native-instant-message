//
//  RNInstantMessage.m
//  InstantMessage
//
//  Created by user on 2017/11/23.
//  Copyright © 2017年 eastcodes. All rights reserved.
//

#import "RNInstantMessage.h"
#import "JFRWebSocket.h"


@interface RNInstantMessage ()<JFRWebSocketDelegate>

@property (nonatomic, strong) JFRWebSocket *socket;

@end


static NSString *const InstantMessageEventName     = @"RNInstantMessage:getInstantMessage";


@implementation RNInstantMessage

RCT_EXPORT_MODULE();

- (NSArray<NSString*> *)supportedEvents {
    return @[InstantMessageEventName];
}

dispatch_queue_t userBackgroundSocketQueue()
{
    static dispatch_once_t queueCreationGuard;
    static dispatch_queue_t socketQueue;
    dispatch_once(&queueCreationGuard, ^{
        socketQueue = dispatch_queue_create("com.bbc.userBackgroundSocketQueue", 0);
    });
    return socketQueue;
}

RCT_EXPORT_METHOD(initWebClient:(NSString *)socketUrl chatSecret:(NSString *)chatSecret connectResolve:(RCTPromiseResolveBlock)resolve connectReject:(RCTPromiseRejectBlock)reject){
    NSLog(@"===== initWebClient ======= ,socketUrl:%@,chatSecret:%@",socketUrl,chatSecret);
    
    @try {
        [self setupWebSocketWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",socketUrl,chatSecret]] andProtocols:nil andCustomQueue:userBackgroundSocketQueue()];
        resolve(@"true");
    } @catch (NSException *exception) {
        reject(@"RNInstantMessage initWebClient",@"connect socket fail!",exception);
    }
    
}

RCT_EXPORT_METHOD(disConnectWebClient){
    [self.socket disconnect];
}

RCT_EXPORT_METHOD(connectWebClient){
    [self.socket connect];
}

RCT_EXPORT_METHOD(sendMessage:(NSString *)message sendResolve:(RCTPromiseResolveBlock)resolve sendReject:(RCTPromiseRejectBlock)reject){
    @try {
        [self.socket writeString:message];
        resolve(@"true");
    } @catch (NSException *exception) {
        reject(@"RNInstantMessage sendMessage",@"send message fail!",exception);
    }
    
}


- (void) setupWebSocketWithURL:(NSURL *)url andProtocols:(NSArray*)protocols andCustomQueue:(dispatch_queue_t)customQueue
{
    self.socket = [[JFRWebSocket alloc] initWithURL:url protocols:protocols queue:customQueue];
    self.socket.delegate = self;
    [self.socket connect];
}


#pragma mark WebSocket delegate
//////////////////////////////////////////////////////////////////////////////
#pragma mark WebSocket Delegate methods.
- (void)websocketDidConnect:(JFRWebSocket*)socket
{
    NSLog(@"websocket is connected");
}

- (void)websocketDidDisconnect:(JFRWebSocket*)socket error:(NSError*)error
{
    NSLog(@"websocket is disconnected: %@", [error localizedDescription]);
    dispatch_async(dispatch_get_main_queue(),^{
        //        self.liveDirection = BBCLivePortrait;
        //        NSNumber *value = [NSNumber numberWithInt:UIDeviceOrientationPortrait];
        //        [[UIDevice currentDevice] setValue:value forKey:@"orientation"];
    });
}


- (void)websocket:(JFRWebSocket*)socket didReceiveMessage:(NSString*)message
{
    NSLog(@"===== didReceiveMessage ===== message: %@", message);
    
    [self sendEventWithName:InstantMessageEventName body:@{@"message":message }];
    
    //    NSError *err = nil;
    
    //    NSMutableDictionary *messageDictionary = [NSJSONSerialization JSONObjectWithData:[message dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:&err];
    //
    //    if(err)
    //    {
    //        NSLog(@"err : %@",err);
    //    }
    
}

- (void)websocket:(JFRWebSocket*)socket didReceiveData:(NSData*)data
{
    NSLog(@"Received data: %@", data);
}


@end
