import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:xwalk/xwalk.dart';

void main() {
  const MethodChannel channel = MethodChannel('xwalk');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Xwalk.platformVersion, '42');
  });
}
