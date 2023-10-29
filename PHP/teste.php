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

    // verificar se não estiver funcionando
    $apelido = "'".$_POST['apelido']."'";

    $sql = "SELECT * FROM podcasts WHERE apelido_autor = $apelido 

    $result = $conn->query($sql);

    if($result->num_rows > 0){
        
        //conteudo
        $registro = mysqli_fetch_array($re  sult);

        $response[""] = $registro['apelido'];


        

    } else{

        $response["mensagem"] = "";
    }

    $conn->close();
        
     } else{

    $sql = "SELECT * FROM usuarios WHERE apelido = $login AND senha = $senha";

    $result = $conn->query($sql);

    if($result->num_rows > 0){
        
        //conteudo
        $registro = mysqli_fetch_array($result);
        $response["apelido"] = $registro['apelido'];
        $response["senha"] = $registro['senha'];
        $response["email"] = $registro['email'];
        $response["tipoUser_id"] = $registro['tipoUser_id'];
        $response["erro"] = false;
        

    } else{

        $response["mensagem"] = "Usuário não existe";
    }

    $conn->close();

        
     }

    
}

echo json_encode($response);

?>