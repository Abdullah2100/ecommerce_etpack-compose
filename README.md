# E-commerce Ecosystem (Jetpack Compose)

This repository contains an e‑commerce mobile solution built with modern Android technologies. It includes two primary Android applications developed in Kotlin and Jetpack Compose: a Customer App and a Delivery Man App. The README below reflects the current codebase structure, key features, and instructions for building and running the projects.

---

## 📁 Repository layout

- `ecommerce_app/` — Customer application (multi-module):
  - `app/` — Compose UI, navigation, and feature wiring
  - `core/` — shared domain and data-layer abstractions
  - other feature modules (e.g., `products`, `cart`, `checkout`) may exist depending on the current refactor

- `ecommerce-delivery-man/` — Delivery personnel application:
  - Delivery assignment screens, navigation, and local encrypted storage
  - QR verification and camera integrations

- `docs/` (optional) — design notes, API contracts, and architecture diagrams

---

## 🚀 Key features (current code)

Customer App
- Jetpack Compose UI (Material 3) and modular architecture.
- Product browsing, search, cart management, and checkout flow.
- Stripe payments integration for secure card payments.
- Address selection and mapping using Google Maps SDK.
- Real-time order updates using SignalR + optional FCM fallbacks.

Delivery Man App
- Real-time delivery assignment and updates via SignalR.
- Encrypted local storage using SQLCipher with Room.
- CameraX-based QR scanning for delivery verification.
- Route display and navigation helpers using Google Maps.
- Cash collection flow for COD orders.

Shared
- Networking via Ktor and/or Retrofit (depending on module). 
- Dependency injection with Koin.
- Local persistence via Room (encrypted where indicated).

---

## 🏗 Architecture & Patterns

- Modularization: feature modules and separation between UI (`app`) and `core` (data/domain) modules for easier testing and scaling.
- Clean-ish separation of concerns: UI (Compose) -> ViewModels -> Domain -> Repository -> Network/DB.
- Real-time coordination handled by SignalR listeners and server events.

---

## ⚙️ Requirements

- Android Studio (Arctic Fox / Bumblebee or later recommended)
- JDK 11+
- Android SDK: compileSdk and target as specified in each module's Gradle files
- Google Maps API key for map features
- Stripe keys for payment flows

---

## 🧭 Build & Run (local)

1. Clone the repo:
   ```bash
   git clone https://github.com/Abdullah2100/ecommerce-Jet-pack-compose.git
   cd ecommerce-Jet-pack-compose
   ```

2. Open the project in Android Studio and allow Gradle to sync.

3. Provide required keys and configuration (local `gradle.properties` or safer secrets store):
   - `GOOGLE_MAPS_API_KEY`
   - `STRIPE_PUBLISHABLE_KEY` (and server-side secret on backend)
   - Any backend base URLs or SignalR endpoints used by the apps

4. Build and run the desired app module (select the appropriate run configuration):
   - `ecommerce_app:app` — Customer app
   - `ecommerce-delivery-man` — Delivery app

5. For debugging real-time features, ensure the backend SignalR server is reachable and any required test accounts/orders are set up.

---

## ✅ Tests & Linting

- Unit tests and instrumented tests are located in each module under `src/test` and `src/androidTest`.
- Run Gradle checks via Android Studio or command line:
  ```bash
  ./gradlew :ecommerce_app:app:connectedAndroidTest
  ./gradlew build
  ```

---

## 📝 Notes about current code (work reflected in this README)

- The customer app has been refactored into a multi-module structure for modularity and faster builds. Expect separate feature modules in `ecommerce_app`.
- Delivery app uses SQLCipher + Room for encrypted local storage and CameraX for QR scanning.
- Payment integration with Stripe is present in the checkout flow; ensure you supply correct keys to test.
- Real-time order/assignment updates are handled by SignalR. FCM may be configured as a fallback push mechanism.

If you'd like, I can add more detailed developer docs (module-level README files), run/CI instructions, or a CONTRIBUTING guide.

---

*Developed by Abdullah.*
