Change Notes

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