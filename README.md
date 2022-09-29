# My To Do List

## Một ứng dụng Android To-do app đơn giản viết bằng kotlin.
Kotlin, Room Database, LiveData, MVVM + Clean Architecture, Coroutine, AlarmManager, Broadcast Receiver, Material Components, Databinding, Navigation Graph
## Screenshots
![image](https://user-images.githubusercontent.com/95533596/193009200-3efbc64c-9a1c-4f68-929d-bdb96c03b4b3.png)
![image](https://user-images.githubusercontent.com/95533596/193009304-2145c576-2453-4e6f-b724-5bd527978683.png)
![image](https://user-images.githubusercontent.com/95533596/193009441-ffdce122-8d09-4669-8585-321761878308.png)
![image](https://user-images.githubusercontent.com/95533596/193009500-414c9642-9680-483b-a0fe-21ad23006d83.png)
![image](https://user-images.githubusercontent.com/95533596/193009697-2f95f9ed-b1fc-4fa8-90e4-99ca2bdc1dda.png)
## Current Features
- Add and manage tasks
- Multiple Task Lists
- Overview of all your tasks at a glance
- Set task due dates and reminders
- Beautiful Material design and interface
- Intuitive sort interface (Giao diện sắp xếp trực quan)
- Data stored offline all locally on your device
## Planned Features
- Dark Mode Toggle/Settings Menu
- Overview customization
- Themes
- Multi Language
- Cloud Data stored
- …
## Project Analysis
![image](https://user-images.githubusercontent.com/95533596/192135715-96b0bfbf-760c-4e7a-9c2f-55fe39646e40.png)
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


