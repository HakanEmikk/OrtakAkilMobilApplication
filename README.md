# Ortak Akıl (Collective Mind)

**Ortak Akıl**, kullanıcıların yapay zeka destekli bir asistana sorular sorabildiği, alınan cevapları diğer kullanıcılarla paylaşarak sosyal bir bilgi havuzu oluşturduğu modern bir Android uygulamasıdır. Kullanıcılar soruları ve cevapları "Keşfet" (Discovery) alanında paylaşabilir, başkalarının paylaşımlarını beğenebilir, yorum yapabilir ve etkileşimde bulunabilir.

## 📱 Özellikler

*   **Yapay Zeka Entegrasyonu**: Kullanıcılar çeşitli kategorilerde sorular sorarak anında yapay zeka destekli cevaplar alabilir.
*   **Sosyal Paylaşım (Keşfet)**: Yapay zeka ile yapılan soru-cevap etkileşimleri "Keşfet" ekranında paylaşılabilir.
*   **Etkileşim**: Diğer kullanıcıların paylaşımlarını beğenme ve yorum yapma özelliği.
*   **Profil Yönetimi**: Kullanıcı profili oluşturma, düzenleme ve profil fotoğrafı yönetimi.
*   **Geçmiş (History)**: Kullanıcının kendi sorduğu tüm sorulara ve aldığı cevaplara erişebildiği geçmiş ekranı.
*   **Oturum Yönetimi**: E-posta/Şifre ile kayıt/giriş ve Google ile Giriş (Google Sign-In) seçenekleri.
*   **Moderasyon**: Rahatsız edici kullanıcıları engelleme ve uygunsuz içerikleri raporlama özellikleri.
*   **Onboarding**: Yeni kullanıcılar için tanıtım ekranları.

## 📸 Ekran Görüntüleri

<p align="center">
  ![image1](https://github.com/user-attachments/assets/ca8b948a-a6d3-423a-8b2c-94b4d71840c2)
  <img src="![image2](https://github.com/user-attachments/assets/dec813f2-10af-405c-854c-3d8ff14d0dbd)
" width="18%" />
  <img src="![image3](https://github.com/user-attachments/assets/12086aa8-03f0-4aef-a229-7fcf16729cd7)
" width="18%" />
  <img src="![image4](https://github.com/user-attachments/assets/d99ffaaf-d2f5-4d08-b39b-e4255fd30441)
" width="18%" />
  <img src="![image5](https://github.com/user-attachments/assets/3273de8d-3457-4bef-8faa-a46db3204c63)
" width="18%" />
</p>


## 🏗️ Mimari ve Tasarım Desenleri

Proje, modern Android geliştirme standartlarına uygun olarak **MVVM (Model-View-ViewModel)** mimarisiyle geliştirilmiştir. 

### Proje Yapısı

Uygulama kodu modüler bir yapıda organize edilmiştir:

*   **`ui/`**: Kullanıcı arayüzü (Jetpack Compose) bileşenlerini ve ekranları içerir. Her ekran kendi alt paketinde (örneğin `login`, `home`, `discovery`) bulunur.
*   **`viewmodel/`**: UI ile veri katmanı arasındaki iletişimi sağlayan, ekran durumlarını (State) yöneten sınıflar yer alır. LiveData ve StateFlow kullanılarak reaktif bir yapı kurulmuştur.
*   **`data/` & `repo/`**: Veri erişim katmanıdır. API çağrılarının yönetildiği ve verilerin işlendiği `Repository` sınıflarını içerir.
    *   `OrtakAkilDaoRepository`: Ana veri işlemlerini yönetir.
    *   `TokenManager`: Kullanıcı oturum token'larını (DataStore ile) güvenli bir şekilde saklar.
*   **`entity/`**: Veri modelleri (Data Classes), ağ istek/cevap modelleri (Request/Response) ve UI durum sınıfları (UiState) bulunur.
*   **`di/`**: **Hilt** (Dependency Injection) modüllerini içerir. `AppModule` içerisinde uygulama genelinde kullanılan bağımlılıklar (Retrofit, OkHttp, Repository vb.) sağlanır.
*   **`retrofit/`**: API servis arayüzleri ve ağ interceptor'ları (örneğin `TokenRefreshInterceptor`) burada tanımlanır.
*   **`helper/`**: Yardımcı sınıflar ve uzantı fonksiyonları (Extensions).
*   **`enums/`**: Uygulama genelinde kullanılan enum sabitleri.

## 🛠️ Kullanılan Teknolojiler ve Kütüphaneler

Uygulama, en güncel Android kütüphaneleri ve teknolojileri kullanılarak geliştirilmiştir:

*   **Programlama Dili**: [Kotlin] (%100)
*   **UI Toolkit**: [Jetpack Compose] (Bildirimsel UI yaklaşımı) ve Material Design 3.
*   **Dependency Injection**: [Hilt] - Bağımlılıkların yönetimi ve enjeksiyonu için.
*   **Ağ İşlemleri (Networking)**:
    *   [Retrofit]: REST API istemcisi.
    *   [OkHttp]: HTTP istemcisi ve Interceptor yönetimi (Token yenileme vb. için).
    *   [Gson]: JSON verilerinin işlenmesi için.
*   **Eşzamansız İşlemler**: [Coroutines] & [Flow].
*   **Yerel Veri Depolama**: [DataStore (Preferences)] - Hafif veri ve token saklama işlemleri için.
*   **Resim Yükleme**: [Coil] - Asenkron resim yükleme ve önbellekleme.
*   **Firebase Entegrasyonu**:
    *   **Firebase Authentication**: Kullanıcı kimlik doğrulama süreçleri.
    *   **Firebase Storage**: Profil fotoğrafları vb. medya dosyalarının depolanması.
    *   **Firebase App Check**: Uygulama güvenliği ve doğrulama.
    *   **Firebase Crashlytics**: Hata takibi ve raporlama.
*   **Diğer**:
    *   **Navigation Compose**: Ekranlar arası geçiş yönetimi.
    *   **WorkManager**: Arka plan işlemleri.
    *   **AndroidX Splash Screen**: Açılış ekranı standardizasyonu.

## � İndir

Uygulamayı Google Play Store üzerinden incelemek için aşağıdaki bağlantıyı kullanabilirsiniz:

[<img src="https://play.google.com/intl/en_us/badges/static/images/badges/tr_badge_web_generic.png" alt="Google Play'den indirin" width="200"/>]()

---
**Geliştirici**: Hakan Emik

## 📄 Lisans

Bu proje, **Hakan Emik** tarafından geliştirilmiş olup, kaynak kodları yalnızca **kişisel portfolyo incelemesi** ve **eğitim** amacıyla paylaşılmıştır. Google Play Store'da yayınlanması planlanmaktadır.

🚫 **Yasal Uyarı**:
*   Bu projenin kaynak kodları, sahibinin yazılı izni olmaksızın kopyalanamaz, dağıtılamaz, değiştirilemez veya ticari bir üründe kullanılamaz.
*   Uygulamanın tüm hakları saklıdır.

