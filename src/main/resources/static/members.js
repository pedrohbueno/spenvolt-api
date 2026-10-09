const API_URL = "http://localhost:8000/api/members";

// CADASTRAR UM MEMBRO
// Recebe os dados do formulário (ou usa o exemplo abaixo se nada for passado).
async function criarMember(member = {
    name: "Membro de teste",
    email: "membro@teste.com",
    photoUrl: "https://example.com/foto.jpg",
    residenceId: 1
}) {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(member)
    });

    if (!response.ok) {
        throw new Error(`Erro ao criar membro: ${response.status} - ${await response.text()}`);
    }

    const resultado = await response.json();
    console.log("Membro criado:", resultado);
    return resultado;
}

// BUSCAR UM MEMBRO PELO ID
async function buscarMember(id) {
    const response = await fetch(`${API_URL}/${id}`);

    if (!response.ok) {
        throw new Error(`Erro ao buscar membro: ${response.status} - ${await response.text()}`);
    }

    const member = await response.json();
    console.log("Membro encontrado:", member);
    return member;
}

// LISTAR MEMBROS DE UMA RESIDÊNCIA
// Rota do controller: GET /api/members?residenceId={id}
async function listarMembersPorResidencia(residenceId) {
    const response = await fetch(`${API_URL}?residenceId=${residenceId}`);

    if (!response.ok) {
        throw new Error(`Erro ao listar membros: ${response.status} - ${await response.text()}`);
    }

    const members = await response.json();
    console.log("Membros da residência:", members);
    return members;
}

// EXEMPLOS DE EXECUÇÃO (não roda mais sozinho: chame executar() no console se quiser testar)
async function executar() {
    try {
        await criarMember();
        await buscarMember(1);
        // await listarMembersPorResidencia(1);
    } catch (error) {
        console.error("Falha na requisição:", error);
    }
}
