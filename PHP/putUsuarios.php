<?php
header('Content-Type: application/json charset=utf-8');

$response = array();
$response["erro"] = true;

if ($_SERVER['REQUEST_METHOD'] == 'POST'){

    include 'dbConnection.php';

    $conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);

    mysqli_set_charset($conn, "Utf8");

    if($conn->connect_error){

        die ("Ops, falhou.");
    }

    $nome = "'".$_POST['nome']."'";
    $email = "'".$_POST['email']."'";
    $senha = "'".$_POST['senha']."'";
    $escolaridade_id = "'".$_POST['escolaridade_id']."'";
    $apelido = "'".$_POST['apelido']."'";
    $cript = "'".$_POST['cript']."'";

    $sql = "SELECT * FROM usuarios WHERE apelido = $apelido OR email = $email";
    
    $result = $conn->query($sql);
    
    if($result->num_rows == 0){


         $sql = "INSERT INTO usuarios (nome, email, senha, tipoUser_id, escolaridade_id, apelido) 
                   VALUES	($nome, $email, AES_ENCRYPT($senha, $cript), $tipoUser_id, $escolaridade_id, $apelido);";


        if ($conn->query($sql) == TRUE) {

            $response["erro"] = false;
            echo json_encode($response);

          } else {

            die ("Ops, falhou.");
          }


    } else{

        $response["mensagem"] = "Usuário já existe";
    }

    $conn->close();
}
?>
