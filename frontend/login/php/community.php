<?php
session_start();
header('content-type:text/html;charset=utf-8');
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "test";

// 实例化mysqli对象，连接mysql数据库
$mysqli = new mysqli($servername, $username, $password, $dbname);

if ($mysqli->connect_errno) {
    die($mysqli->connect_error);
}
$mysqli->set_charset('utf8'); // 设置字符

validateUser($mysqli);

$mysqli->close();

function validateUser($mysqli)
{
    $username = $_POST['signin-username'];
    $password = $_POST['signin-password'];

    // 查询数据库，获取存储的哈希密码和盐值
    $sql = "SELECT username, email, sex, introduction, password, salt FROM user WHERE username = ?";
    $stmt = $mysqli->prepare($sql);
    $stmt->bind_param('s', $username);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $username = $row['username'];
        $dbPassword = $row['password'];
        $salt = $row['salt'];

        // 使用获取的盐值和用户输入的密码进行哈希运算
        $hashedPassword = hashPassword($password, $salt);

        // 将生成的哈希值与数据库中存储的哈希密码进行比较，以验证密码是否匹配
        if ($hashedPassword === $dbPassword) {
            // 邮箱和密码匹配成功，进行页面重定向
            echo "<script>alert('登录成功');window.location.href='../index.html'</script>";
            exit(); // 确保重定向后的代码不会继续执行
        } else {
            // 密码错误，显示错误消息
            echo "<script>alert('密码错误！');window.location.href='../login.html'</script>";
        }
    } else {
        // 用户不存在，显示错误消息
        echo "<script>alert('用户不存在！');window.location.href='../login.html'</script>";
    }

    $stmt->close();
}

function hashPassword($password, $salt)
{
    // 使用盐值和密码进行哈希运算
    $saltedPassword = $salt . $password; // 将盐值和密码拼接起来
    return hash('sha256', $saltedPassword); // 返回哈希后的密码
}

