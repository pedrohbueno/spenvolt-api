const API_URL = "http://localhost:8080/api/users";

// CADASTRAR UM USUÁRIO
// Recebe os dados do formulário (ou usa o exemplo abaixo se nada for passado).
async function criarUser(user = {
    name: "pedrohbueno",
    email: "pedrohbueno.contato@gmail.com",
    password: "11fefe8123f8h7",
    photoUrl: "https://avatars.githubusercontent.com/u/153121748?v=4"
}) {
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

// EXEMPLOS DE EXECUÇÃO (não roda mais sozinho: chame executar() no console se quiser testar)
async function executar() {
    try {
        const userCriado = await criarUser();

        if (userCriado.id != null) {
            await buscarUser(userCriado.id);
        } else {
            console.log("A resposta não contém o campo 'id'.");
        }
    } catch (error) {
        console.error("Falha na requisição:", error);
    }
}
