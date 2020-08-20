#import "XwalkPlugin.h"
#if __has_include(<xwalk/xwalk-Swift.h>)
#import <xwalk/xwalk-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "xwalk-Swift.h"
#endif

@implementation XwalkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftXwalkPlugin registerWithRegistrar:registrar];
}
@end
