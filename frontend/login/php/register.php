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
$mysqli->set_charset('utf8'); // 设置字符集

signup($mysqli);

$mysqli->close();

function signup($mysqli)
{
    $username = $_POST["signup-name"];
    $email = $_POST["signup-email"];
    $password = $_POST["signup-password"];

    // 先查询数据库，检查用户名是否已存在
    $query = "SELECT * FROM user WHERE username = ?";
    $stmt = $mysqli->prepare($query);
    $stmt->bind_param('s', $username);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        echo "<script>alert('用户名已存在，请选择其他用户名');window.location.href='../login.html'</script>";
        return; // 如果用户名已存在，则停止执行后续代码
    }

    // 生成盐值
    $salt = generateSalt();

    // 使用盐值和密码进行哈希运算
    $hashedPassword = hashPassword($password, $salt);

    // 将用户名、邮箱、哈希密码和盐值存储到数据库
    $sql = "INSERT INTO user (username, email, password, salt) VALUES (?, ?, ?, ?)";
    $stmt = $mysqli->prepare($sql);
    $stmt->bind_param('ssss', $username, $email, $hashedPassword, $salt);

    if ($stmt->execute()) {
        echo $stmt->insert_id; // 程序成功，返回插入数据表的行id
        echo PHP_EOL;
        echo "<script>alert('注册成功');window.location.href='../login.html'</script>";
    } else {
        echo $stmt->error; // 执行失败，显示错误信息
    }

    $stmt->close();
}

function generateSalt()
{
    // 生成一个随机的盐值
    return bin2hex(random_bytes(16));
}

function hashPassword($password, $salt)
{
    // 使用盐值和密码进行哈希运算
    $saltedPassword = $salt . $password; // 将盐值和密码拼接起来
    return hash('sha256', $saltedPassword); // 返回哈希后的密码
}
