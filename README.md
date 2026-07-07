### Team Member ###
1. Đào Châu Giang ID: 079206001855
   Github: YangDao304
   
2. Phạm Thanh Hải ID: 079206031801
   Github: thanhhai36343-a11y

3. Nguyễn Chí Hải ID: 079206000749 
   Github: Dno-0749

4. Phạm Thái Bình ID: 080206000249 
   Github: phamthaibinh777

5. Phạm Văn Tuấn ID: 068206008212 
   Github: phamtuan77

6. Lê Thanh Vinh ID: 064206005782 
   Github: lethanhvinh050106

### Project Description ###
- This project is a GUI-based chat application using TCP Socket Programming. 
- The application allows multiple users to connect to a server and communicate in real time through a desktop graphical user interface. 
### Features ###
- GUI Login 
- TCP Client-Server Connection 
- Multi-client Chat 
- Real-time Messaging 
- Online User List 
- Join/Leave Notification 
- Timestamp Message 
- Disconnect Handling 
- Private Message (bonus) 
- File Transfer 
- Chat History Saving 
- Dark Mode GUI 
### Teachnology Stack ###
- Java
- Java Swing
- TCP Socket
- Multithreading
- Github
### Project Structure ###
```text
TCP-CHAT-APP/
│
├── Code/                  # Source code and batch files
│   ├── client/            # Client connection logic
│   ├── gui/               # Swing user interfaces
│   ├── server/            # TCP server implementation
│   ├── util/              # Utility classes
│   ├── RunServer.bat      # Compile and start the server
│   └── RunClient.bat      # Compile and start a client
│
├── DOCX/                  # Project documentation
├── PPTX/                  # Presentation slides
├── Extra/                 # Additional project resources
│
└── README.md
```
### System Requirements ###
- Java JDK 17 or later
- Windows 10/11
- Command Prompt (CMD)
- Visual Studio Code (recommended)
### How to Run ###
1. Open terminal
2. Enter **cd code**
3. Enter **.\Runserver.bat**
4. Enter the server port (press **Enter** to use the default port **1234**).
5. Enter **.\Run Client.bat**.
6. Enter your username, server IP address, and port.
7. Open additional client windows by open another terminal and enter **.\Run Client.bat** again if you want to test multiple users.
### Main Functions ###
### Login
- Username validation
- IP Address validation
- Port validation
### Chat
- Public Chat
- Private Message
- Reply Message
- Forward Message
### User Interface
- Online User List
- User Avatar
- Dark / Light Theme
### Test Cases ###
The application has been tested with the following scenarios:
- Server Start
- Client Connection
- Login Validation
- Invalid IP Address
- Invalid Port
- Broadcast Message
- Private Message
- Reply Message
- Forward Message
- Multiple Clients Join Chat Room
- Online User List Update
- Empty Message
- Long Message
- User Avatar Display
- Dark / Light Theme
- Reply Cancellation
- Consecutive Messages
### Authors ###
- Group 9 
- Course: Network Programming
- Language: Java
