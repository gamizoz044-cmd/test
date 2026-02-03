# RP Marketplace

Plataforma web de vendas para scripts e modelos de Roleplay (RP), com foco em segurança, organização e escalabilidade.

## Principais recursos
- Cadastro, login seguro com JWT e recuperação de senha por e-mail.
- Catálogo público de produtos e páginas detalhadas.
- Carrinho e pedidos com histórico do cliente.
- Painel de vendedores para gestão de produtos e vendas.
- Painel administrativo para moderação e gestão de usuários.
- Área de comunidade com posts e comentários.
- Chat em tempo real via WebSocket.
- Páginas legais obrigatórias.

## Requisitos
- Java 17
- MySQL 8

## Configuração rápida
1. Ajuste `src/main/resources/application.yml` com suas credenciais e SMTP.
2. Execute `mvn spring-boot:run`.

## Endpoints principais
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/password-reset`
- `POST /api/auth/password-reset/confirm`
- `GET /api/products/public`
- `GET /api/products/public/{id}`
- `POST /api/orders`
- `GET /api/orders`
- `POST /api/community/posts`
- `GET /api/community/public/posts`
- `POST /api/support/contact`
- `GET /api/legal/terms`
