Release notes

0.3.3
 * Supporting of VPAID 2.0
 * Fixed: fullscreen 'move event' that causes ad click
 * Fixed: 'remaining time' position

0.3.1
 * Implemented VAST 2.0
 * Added is #isLoaded() method to InterstitialAdContainer
 * Updated SampleApp
 * Fixed ad autoclick issue on banner cache phase

0.2.8
 * Fixed NPE on restart IntentService

0.2.7
 * Implemented ad caching functionality
 * Created SdkConfig class for providing caching settings

0.2.6
 * Implemented prerender functionality for banners
 * Stable behaviour of the interstitial
 * Fixed banner autorefresh when screen switched off
 * Fixed long time banner loading
 * Fixed app crash on collect device info
 * Banner will not be shown if get any error during loading
 * Added InterstitialListener with #closed() that called on interstitial closed event

0.2.2
 * Added interstitial banner
 * Added method BannerContainer#setExpirationTime(int) that allows to specify refresh time for banner
 * Added attribute _smartyads:refreshTime="20"_ that allows to specify refresh time for BannerContainer via layout.xml