SeekhoAnime — README

Project overview

This is a simple Android app that shows top and popular anime retrieved from the Jikan API (https://api.jikan.moe).
The UI is built with Jetpack Compose. Data is cached locally so previously viewed content is available offline.

Features implemented

- Home: View a scrollable list of top anime.
  - Each item shows: title, poster image, episode count (when available).
  - Tap an item to open the details page.

- Details: View extended information about an anime.
  - Shows title, synopsis, genres, main cast, episode count, and rating.
  - Trailer: if a YouTube trailer is available the app plays it in\-app using an embedded YouTube player.
  - If the trailer can't be played in\-app, there's a fallback ui added through which the user can view the trailer on youtube app.
  - If no trailer is available, the poster image is shown instead.

- Offline / caching:
  - Once anime lists or details have been fetched, they are stored locally and remain viewable when offline.
  - When the device is online the app will attempt to refresh data and update cached content.

- Basic error handling and feedback:
  - Network failures show a simple error state (no retry option added yet).
  - Loading indicators appear while fetching data.

Corner cases (what you, as an end\-user, can expect)

- First run with no internet:
  - The app cannot fetch the anime list on first run without internet; the list will be empty and an error message is shown. Connect to the internet and retry.

- Partial metadata:
  - Some anime entries may lack episode counts, scores or cast information. Fields that are missing will be omitted or shown as "N/A".

- Trailer not available or non\-YouTube trailer:
  - If no YouTube trailer is provided the details screen shows the poster image instead of a video player.
  - Some embed links that are not standard YouTube may not play; the app prefers YouTube embeds.

- Stale data while offline:
  - Cached data reflects the last successful sync. If the API changed since then you will see older information until the app reconnects.

- Rate limiting / API errors:
  - If the Jikan API rate limits requests or returns an error, the app will surface a network error and keep showing cached data (if any).

Assumptions made (feature level)

- You have an internet connection for the app to fetch the initial list and to refresh trailers and images.
- The initial fetch populates the local cache; after that core browsing works offline.
- Trailers are expected to be YouTube links for in\-app playback. Non\-YouTube trailers will fall back to showing images.
- The app relies on external poster image hosting; missing images are possible if the source is unavailable.
- Occasional API downtime or rate limiting may temporarily reduce functionality (list not updating, trailers failing).

Quick end\-user tips

- If the list is empty: check network or restart the app.
- If a trailer won't play: wait and try again later (API or network issues may be temporary).
