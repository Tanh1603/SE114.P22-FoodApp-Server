# SE114.P22-FoodApp-Server

## Giới thiệu

Đây là project server cho ứng dụng FoodApp, sử dụng Spring Boot, Maven, PostgreSQL, Redis, Firebase, Cloudinary, v.v.

## Yêu cầu hệ thống

- Java 21 trở lên
- Maven 3.8+
- PostgreSQL

## Hướng dẫn cài đặt

### 1. Clone project

```sh
git clone https://github.com/Tanh1603/SE114.P22-FoodApp-Server.git
cd SE114.P22-FoodApp-Server/server
```

### 2. Tạo file biến môi trường

Tạo các biến môi trường với nội dung mẫu:

env

          "DATABASE_URL": "",
          "DATABASE_USERNAME": "",
          "DATABASE_PASSWORD": "",
          "FIREBASE_PROJECT_ID": "",
          "FIREBASE_SERVICE_ACCOUNT_BASE64:"":

          "CLOUDINARY_CLOUD_NAME": "",
          "CLOUDINARY_API_KEY": "",
          "CLOUDINARY_FOLDER": "",
          "CLOUDINARY_API_SECRET": "",

          "ADMIN_EMAIL": "",
          "ADMIN_PASSWORD": "",
          "STAFF_EMAIL": "",
          "STAFF_PASSWORD": "",
          "SHIPPER_EMAIL": "",
          "SHIPPER_PASSWORD": "",

          "SPRING_AI_OPENAI_CHAT_BASE_URL": "",
          "SPRING_AI_OPENAI_CHAT_OPTIONS_MODEL": ",
          "SPRING_AI_MISTRALAI_API_KEY": ""


- Đảm bảo đã cấu hình đúng thông tin kết nối database, redis (nếu dùng), và các biến môi trường khác.
- Encode base64 file `serviceAccountKey.json` của Firebase và đặt vào `FIREBASE_SERVICE_ACCOUNT_BASE64` .

### 3. Cài đặt dependencies

```sh
mvnw.cmd clean install
```

### 4. Chạy project

```sh
mvnw.cmd spring-boot:run
```

Project sẽ chạy tại `http://localhost:8080` (hoặc cổng bạn cấu hình).

## Tham khảo thêm

- Xem file `HELP.md` để biết thêm tài liệu tham khảo về Spring Boot, Maven, v.v.

---

Nếu gặp lỗi, kiểm tra lại biến môi trường, cấu hình database, và log của ứng dụng.
