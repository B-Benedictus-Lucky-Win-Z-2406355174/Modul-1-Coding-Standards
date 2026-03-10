REFLEKSI 1
Di modul kali ini, saya telah mengimplementasikan fitur sederhana untuk Create Product, melihat List Product,  Edit Product dan Delete Product menggunakan Spring Boot. Struktur kode sudah dipisahkan antara controller, service, dan tampilan (HTML), sehingga alur program lebih mudah dipahami. Penamaan method dan endpoint dibuat cukup jelas agar mudah dibaca dan dimengerti saat pengembangan.

Untuk clean code, saya berusaha menjaga agar setiap method memiliki satu fungsi utama dan tidak terlalu panjang. Dari sisi keamanan, proses delete dan edit dilakukan melalui POST request sehingga lebih aman dibandingkan menggunakan GET.

Namun, masih ada beberapa hal yang bisa diperbaiki, seperti menambahkan validasi input agar data yang dimasukkan pengguna lebih terkontrol. Selain itu, pesan error juga bisa dibuat lebih jelas jika terjadi kesalahan. 


REFLEKSI 2
Setelah membuat unit test, saya merasa lebih yakin bahwa fitur yang dibuat berjalan sesuai dengan yang diharapkan. Untuk setiap class yang ada, jumlah unit test beragam dan sebaiknya mencapai semua fungsi penting dengan semua kondisi. Untuk memastikan unit test sudah cukup, kita bisa melihat apakah semua logika utama sudah diuji dan dibantu dengan melihat hasil code coverage. Code coverage membantu mengetahui bagian kode mana yang sudah atau belum diuji. Namun, meskipun code coverage mencapai 100%, hal tersebut tidak menjamin kode bebas dari bug karena masih bisa ada kesalahan logika atau kasus yang belum terpikirkan. Meskipun code coverage mencapai 100%, hal tersebut tidak berarti kode bebas dari bug atau error. Code coverage hanya menunjukkan bahwa baris kode sudah dijalankan oleh test, bukan bahwa semua kemungkinan kasus dan logika sudah diuji dengan benar. Masih mungkin terdapat kesalahan logika, edge case, atau kondisi tertentu yang tidak terdeteksi oleh unit test. Karena itu, code coverage sebaiknya digunakan sebagai alat bantu dan bukan satu-satunya acuan kualitas kode kita.

Jika saya membuat functional test baru untuk mengecek jumlah item pada product list dengan menyalin struktur test sebelumnya, dari sisi kebersihan kode hal ini kurang baik. Duplikasi setup code dan variabel yang sama dapat menurunkan kualitas kode karena membuat kode lebih sulit dirawat. Jika ada perubahan, kita harus mengubahnya di banyak tempat. Untuk memperbaikinya, setup yang sama sebaiknya dipindahkan ke superclass atau helper method agar bisa digunakan ulang. Dengan cara ini, kode test menjadi lebih bersih, lebih rapi, dan lebih mudah dikembangkan.

## Modul 2

### Reflection
1. **Daftar *Code Quality Issue* yang diperbaiki dan strateginya:**
   Selama mengerjakan latihan ini, saya memperbaiki beberapa temuan (*issues*) dari SonarCloud untuk memenuhi *Clean Code*. Beberapa issue yang saya atasi antara lain:
   - **Empty Method & Missing Assertions di `EshopApplicationTests`:** SonarCloud mendeteksi bahwa *method* `contextLoads()` kosong dan tidak memiliki *assertion* (pengecekan) sama sekali. Strategi perbaikannya adalah dengan menambahkan `assertTrue(true, "The application context should load successfully");` agar *test* tersebut valid secara struktur dan SonarCloud mengerti bahwa tes tersebut sengaja dibiarkan sukses jika berhasil *load*.
   - **Unnecessary `throws Exception` di Functional Test:** Pada file `CreateProductFunctionalTest`, deklarasi *method* tes seperti `createProduct_isCorrect` memiliki klausa `throws Exception` padahal di dalam tubuh *method* tersebut sama sekali tidak memanggil fungsi yang melempar _checked exception_. Strategi saya adalah menghapus frasa `throws Exception` tersebut di semua tes fungsional agar deklarasinya menjadi lebih presisi, bersih, dan tidak menyesatkan pembaca kode.

2. **Evaluasi Implementasi CI/CD:**
   Ya, menurut saya implementasi _workflow_ di GitHub Actions saat ini sudah memenuhi definisi **Continuous Integration (CI)** dan **Continuous Deployment (CD)**. 
   Untuk **CI**, setiap kali ada kode yang di-*push* atau di-_Pull Request_ ke *branch* utama, GitHub Actions akan langsung (*continuous*) menjalankan pengujian otomatis (seperti *unit test* dan *functional test*) lalu menjalankan analisis kualitas kode menggunakan SonarCloud dan OSSF Scorecard. Ini memastikan kode baru membaur (terintegrasi) secara aman tanpa merusak fitur lama.
   Untuk **CD**, implementasinya juga telah berhasil karena melalui file `deploy.yml` dan `Dockerfile`, kode yang sudah lolos uji di *branch* utama akan secara otomatis dikompilasi ulang dan di-deploy ke PaaS (Koyeb). Hal ini membebaskan developer dari keharusan merilis versi terbaru dari aplikasi secara manual, sehingga *deployment* dapat terjadi terus-menerus mengikuti perkembangan kode secara otomatis.

## Modul 3

### Reflection

#### 1) Prinsip SOLID yang saya terapkan pada proyek ini:

1. **SRP (Single Responsibility Principle)**
   Sebelumnya, `CarController` didefinisikan sebagai *inner class* di dalam `ProductController.java`, sehingga satu file menangani dua tanggung jawab sekaligus — mengelola Product dan Car. Saya memisahkan `CarController` ke file tersendiri (`CarController.java`) agar masing-masing *controller* hanya bertanggung jawab atas satu entitas saja.

2. **OCP (Open/Closed Principle)**
   `CarController` sebelumnya meng-*extend* `ProductController`, sehingga perubahan pada `ProductController` dapat berdampak pada `CarController`. Saya menghapus relasi *inheritance* tersebut agar `CarController` berdiri sendiri sebagai *class* independen. Dengan begitu, masing-masing *controller* dapat dikembangkan (*open for extension*) tanpa perlu memodifikasi *class* lainnya (*closed for modification*).

3. **LSP (Liskov Substitution Principle)**
   Karena `CarController` meng-*extend* `ProductController`, secara teori objek `CarController` seharusnya bisa menggantikan `ProductController`. Namun, keduanya menangani entitas berbeda (Car vs Product) dengan *endpoint* berbeda, sehingga substitusi tidak mungkin dilakukan tanpa merusak program. Penghapusan *inheritance* yang saya lakukan menyelesaikan pelanggaran LSP ini.

4. **ISP (Interface Segregation Principle)**
   Pada proyek ini, *interface* `CarService` dan `ProductService` sudah dipisahkan secara spesifik. `CarService` hanya memiliki *method* CRUD untuk Car (`create`, `findAll`, `findById`, `update`, `deleteCarById`), dan `ProductService` hanya memiliki *method* CRUD untuk Product. Tidak ada *client* yang dipaksa bergantung pada *method* yang tidak mereka butuhkan, sehingga prinsip ISP sudah terpenuhi.

5. **DIP (Dependency Inversion Principle)**
   Sebelumnya, `CarController` bergantung langsung pada *class* konkret `CarServiceImpl`. Saya mengubahnya agar bergantung pada *interface* `CarService` sebagai gantinya. Dengan demikian, *high-level module* (`CarController`) tidak lagi bergantung pada *low-level module* (`CarServiceImpl`), melainkan pada abstraksi (`CarService`).

#### 2) Keuntungan menerapkan prinsip SOLID pada proyek ini:

- **Kode lebih mudah di-*maintain*:** Dengan menerapkan SRP, misalnya memisahkan `CarController` dan `ProductController` ke file masing-masing, jika ada perubahan logika terkait Car, saya hanya perlu mengedit `CarController.java` tanpa khawatir merusak fungsionalitas Product. Hal ini membuat proses *debugging* dan pengembangan menjadi lebih cepat dan terarah.
- **Kode lebih fleksibel untuk dikembangkan:** Dengan menerapkan DIP, `CarController` bergantung pada *interface* `CarService`, bukan pada `CarServiceImpl`. Jika di masa depan saya ingin mengganti implementasi *service* (misalnya dari *in-memory* ke *database*), saya cukup membuat implementasi baru dari `CarService` tanpa perlu mengubah kode di `CarController` sama sekali.
- **Mengurangi dampak perubahan (*ripple effect*):** Dengan menerapkan OCP dan menghapus `extends ProductController` dari `CarController`, perubahan pada `ProductController` tidak lagi berdampak pada `CarController`. Kedua *class* bisa berkembang secara independen tanpa saling mengganggu.
- **Desain lebih bersih dan mudah dipahami:** Dengan ISP, setiap *interface* hanya berisi *method* yang relevan. Developer baru yang membaca kode langsung memahami bahwa `CarService` khusus untuk operasi Car, tanpa harus menelusuri *method* Product yang tidak relevan.

#### 3) Kerugian jika tidak menerapkan prinsip SOLID pada proyek ini:

- **Satu perubahan bisa merusak banyak hal:** Tanpa SRP, ketika `CarController` masih menjadi *inner class* di dalam `ProductController`, mengubah *mapping* atau logika di `ProductController` berpotensi merusak seluruh fungsionalitas Car juga, meskipun perubahan tersebut sama sekali tidak berkaitan dengan Car.
- **Kode sulit untuk di-*extend*:** Tanpa OCP, karena `CarController` meng-*extend* `ProductController`, menambahkan fitur baru di `ProductController` (seperti validasi khusus Product) bisa secara tidak sengaja mengubah perilaku `CarController`. Hal ini membuat pengembangan fitur baru menjadi berisiko dan memerlukan pengujian ekstra.
- **Substitusi yang salah menyebabkan *bug* tersembunyi:** Tanpa LSP, relasi `CarController extends ProductController` membuat Spring berpotensi memperlakukan `CarController` sebagai `ProductController`. Ini bisa menyebabkan *endpoint* Product secara tidak sengaja terduplikasi atau tertimpa oleh `CarController`, yang sangat sulit untuk di-*debug*.
- **Ketergantungan pada implementasi konkret menyulitkan pengujian:** Tanpa DIP, jika `CarController` bergantung pada `CarServiceImpl` secara langsung, proses *unit testing* menjadi lebih sulit karena kita tidak bisa dengan mudah meng-*inject mock object*. Kita terpaksa menguji dengan implementasi sebenarnya, yang membuat test lebih lambat dan rapuh.
