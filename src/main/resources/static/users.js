const API_URL = "http://localhost:8000/api/users";

// CADASTRAR UM USUÁRIO
async function criarUser() {
    const user = {
        name: "pedrohbueno",
        email: "pedrohbueno.contato@gmail.com",
        photoUrl: "https://avatars.githubusercontent.com/u/153121748?v=4",
        active: true,
        last_login_at: new Date().toISOString(),
        password_hash: "11fefe8123f8h7"
    };

    const response = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    });

    if (!response.ok) {
        throw new Error(
            `Erro ao criar usuário: ${response.status} - ${await response.text()}`
        );
    }

    const resultado = await response.json();
    console.log("Usuário criado:", resultado);

    return resultado;
}

// BUSCAR UM USUÁRIO PELO ID
async function buscarUser(id) {
    const response = await fetch(`${API_URL}/${id}`);

    if (!response.ok) {
        throw new Error(
            `Erro ao buscar usuário: ${response.status} - ${await response.text()}`
        );
    }

    const user = await response.json();
    console.log("Usuário encontrado:", user);

    return user;
}

// EXEMPLOS DE EXECUÇÃO
async function executar() {
    try {
        // Cadastra um usuário
        const userCriado = await criarUser();

        // Busca o usuário pelo ID retornado pela API
        if (userCriado.id != null) {
            await buscarUser(userCriado.id);
        } else {
            console.log("A resposta não contém o campo 'id'.");
        }
    } catch (error) {
        console.error("Falha na requisição:", error);
    }
}

executar();