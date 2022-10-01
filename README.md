# My To Do List

## Một ứng dụng Android To-do app đơn giản viết bằng kotlin.
Kotlin, Room Database, LiveData, MVVM + Clean Architecture, Coroutine, AlarmManager, Broadcast Receiver, Material Components, Databinding, Navigation Graph
## Current Features
- Add, Edit, Delete and manage tasks
- Multiple Task Lists
- Search Tasks
- Overview of all your tasks at a glance
- Set task due dates and reminders
- Beautiful Material design and interface
- Intuitive filter interface (Giao diện lọc trực quan)
- Data stored offline all locally on your device
- Multi Language
- Light/Dark Mode
- Date multi format,...
## Over View
|<img width="200" src="https://user-images.githubusercontent.com/95533596/193405757-7d078c7f-019d-48a6-8e69-53dbeb2a2eab.png">
<img width="200" src="https://user-images.githubusercontent.com/95533596/193406155-eb763090-a162-41ca-ac35-c2f28b6c7247.png">
<img width="200" src="https://user-images.githubusercontent.com/95533596/193406167-8997edae-0b92-4eb0-98d5-1244eabf176b.png">
<img width="200" src="https://user-images.githubusercontent.com/95533596/193406348-b3254af7-e6b8-4504-8ed8-c550979ece01.png">
<img width="200" src="https://user-images.githubusercontent.com/95533596/193406189-31fadd14-4c0a-4f60-88ed-2a9b868aab9d.png">
<img width="200" src="https://user-images.githubusercontent.com/95533596/193406212-5776b4f8-3978-4e2f-b750-94e78bb5f094.png">
<img width="200" src="https://user-images.githubusercontent.com/95533596/193406826-db32fd97-811a-4b4d-be95-74545a900ede.png">

### Dark Mode
|<img width="200" src="https://user-images.githubusercontent.com/95533596/193406408-13b77007-fb16-4d79-aaa5-e62b6742d648.png">
<img width="200" src="https://user-images.githubusercontent.com/95533596/193406414-d9614686-a5c4-4c25-bfea-40f9d69bd757.png">
<img width="200" src="https://user-images.githubusercontent.com/95533596/193406423-1da68f9c-80a3-43b0-b96e-4888e9ceee83.png">

### Multi Language
Tiếng Việt         <img width="200" src="https://user-images.githubusercontent.com/95533596/193406481-ac80ad8e-e054-44e9-90c9-27bd9b9477da.png">
<img width="200" src="https://user-images.githubusercontent.com/95533596/193406555-a9a2af70-faf0-4945-a384-da5695e0ee5e.png">

Tiếng Anh     <img width="200" src="https://user-images.githubusercontent.com/95533596/193406583-22fd9dc3-8993-4dfd-b781-633337b50c64.png">
<img width="200" src="https://user-images.githubusercontent.com/95533596/193406588-997e1495-0064-48fe-8baf-914b20b84949.png">

### Notification
![image](https://user-images.githubusercontent.com/95533596/193406632-6afd2e51-6ad8-4ccd-b7c1-4817ea1a51e5.png)
![image](https://user-images.githubusercontent.com/95533596/193406661-353701ea-cde9-48c7-b3f1-374f92fc18c3.png)

## Planned Features
- Overview customization
- Themes
- Cloud Data stored
- …
## Project Analysis
![image](https://user-images.githubusercontent.com/95533596/193406755-0f2b324d-57c2-4579-8585-5b47550251dc.png)
### di ###
![image](https://user-images.githubusercontent.com/95533596/192136132-d85e9508-c962-4d56-8dc7-2c286bb0739a.png)
- Chứa các instance của các Repository và các UseCases.
### data ###
![image](https://user-images.githubusercontent.com/95533596/192136289-a88960a0-d093-4f4b-adaa-27a41cce9805.png)
- Chứa database và các logic về thêm, sửa, xóa, cập nhật data...
### domain ###
![image](https://user-images.githubusercontent.com/95533596/192136493-4b61f9ff-bf0f-47e0-a2c7-5d26a0f52100.png)
- Chứa các data, logic về business logic ( logic chức năng,...)
#### model ####
![image](https://user-images.githubusercontent.com/95533596/192136836-091ddec5-59a5-44a7-86da-5b2a5890f1fa.png)
- Chứa thông tin của các đối tượng todo, group, logic cơ bản của đối tượng.
#### service ####
![image](https://user-images.githubusercontent.com/95533596/192136978-ad7b8e61-d73b-4304-b2c6-1916ee727693.png)
- Chứa các service, ở đây là Alarm Manager
- Gồm các chức năng: Đặt thông báo, hủy thông báo, cập nhật lại thông báo khi Android Reboot
#### use_case ####
![image](https://user-images.githubusercontent.com/95533596/192136975-a10d67fd-4952-4e14-9c5d-412a4b5274ee.png)
- Gồm các chức năng của tính năng app. (thêm, sửa, xóa ,get data,... kèm trong đó là các business logic)
#### util ####
![image](https://user-images.githubusercontent.com/95533596/192137012-bc4b63b1-bf79-4c5f-819a-92977b190b95.png)
- Chứa các class, interface, logic bổ trợ cho service hoặc use_case.
### ui ###
![image](https://user-images.githubusercontent.com/95533596/192137040-87d34806-4a3f-446e-8dfb-64045d01b104.png)
- Chứa ui và viewmodel.
- My to do app sử dụng 2 fragment(add_edit_todo và home) và 1 activity.
## Ứng dụng hướng tới tối đa hóa tính trừu tượng, bảo trì và tái sử dụng code <3
