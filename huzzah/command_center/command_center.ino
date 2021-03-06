

// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

// This example only works with Arduino IDE 1.6.8 or later.

#include <ESP8266WiFi.h>
#include <time.h>
#include "command_center.h"
#include <AzureIoTHub.h>
#include <AzureIoTHubClient.h>
#include <AzureIoTProtocol_MQTT.h>

#ifdef ARDUINO_ARCH_ESP8266
static WiFiClientSecure sslClient; // for ESP8266
#elif ARDUINO_SAMD_FEATHER_M0
static Adafruit_WINC1500SSLClient sslClient; // for Adafruit WINC1500
#else
static WiFiSSLClient sslClient;
#endif

/*
   The new version of AzureIoTHub library changed the AzureIoTHubClient signature.
   As a temporary solution, we will test the definition of AzureIoTHubVersion, which is only defined
      in the new AzureIoTHub library version. Once we totally deprecate the last version, we can take
      the ‘#ifdef’ out.
*/
#ifdef AzureIoTHubVersion
static AzureIoTHubClient iotHubClient;
#else
AzureIoTHubClient iotHubClient(sslClient);
#endif

const char ssid[] = "InternetUNE2"; //  your WiFi SSID (name)
const char pass[] = "Microsoft2017";    // your WiFi password (use for WPA, or use as key for WEP)
const char connectionString[] = "HostName=fgIoTHubTest.azure-devices.net;DeviceId=beakerADevice;SharedAccessKey=ivMVshMt0tRGo08W1es5XQ==";

int status = WL_IDLE_STATUS;

///////////////////////////////////////////////////////////////////////////////////////////////////
void setup() {
  initSerial();
  initWifi();
  initTime();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
void loop() {
  // This function doesn't exit.
  command_center_run();
}

///////////////////////////////////////////////////////////////////////////////////////////////////
void initSerial() {
  // Start serial and initialize stdout
  Serial.begin(115200);
  Serial.setDebugOutput(true);
}

///////////////////////////////////////////////////////////////////////////////////////////////////
void initWifi() {
  // Attempt to connect to Wifi network:
  Serial.print("Attempting to connect to SSID: ");
  Serial.println(ssid);

  // Connect to WPA/WPA2 network. Change this line if using open or WEP network:
  status = WiFi.begin(ssid, pass);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("Connected to wifi");
}

///////////////////////////////////////////////////////////////////////////////////////////////////
void initTime() {
  time_t epochTime;

  configTime(0, 0, "pool.ntp.org", "time.nist.gov");

  while (true) {
    epochTime = time(NULL);

    if (epochTime == 0) {
      Serial.println("Fetching NTP epoch time failed! Waiting 2 seconds to retry.");
      delay(2000);
    } else {
      Serial.print("Fetched NTP epoch time is: ");
      Serial.println(epochTime);
      break;
    }
  }
}

