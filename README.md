# API Gateway com Spring Cloud Gateway

Este projeto implementa uma API Gateway robusta usando Spring Cloud Gateway, fornecendo funcionalidades essenciais como roteamento, autenticação, rate limiting, circuit breaker e monitoramento.

## 🚀 Funcionalidades

- **Roteamento Inteligente**: Roteamento baseado em path com descoberta automática de serviços
- **Autenticação JWT**: Validação de tokens JWT com filtros customizados
- **Rate Limiting**: Limitação de taxa por usuário, IP e endpoint usando Redis
- **Circuit Breaker**: Proteção contra falhas de serviços com fallback automático
- **Service Discovery**: Integração com Eureka para descoberta automática de serviços
- **Logging Avançado**: Logs detalhados de requisições e respostas
- **Monitoramento**: Métricas com Prometheus e health checks
- **CORS**: Configuração global de CORS para aplicações web

## 🏗️ Arquitetura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client App    │───▶│   API Gateway   │───▶│  Microservices  │
│                 │    │                 │    │                 │
│ - Web App       │    │ - Routing       │    │ - User Service  │
│ - Mobile App    │    │ - Auth          │    │ - Product Svc   │
│ - API Client    │    │ - Rate Limit    │    │ - Order Service │
└─────────────────┘    │ - Circuit Br.   │    │ - Auth Service  │
                       │ - Logging       │    └─────────────────┘
                       └─────────────────┘
```

## 📋 Pré-requisitos

- Java 17+
- Maven 3.6+
- Redis (para rate limiting)
- Eureka Server (para service discovery)

## 🛠️ Instalação e Execução

### 1. Clone o repositório
```bash
git clone <repository-url>
cd api-gateway-springboot
```

### 2. Configure o Redis
```bash
# Instalar Redis (Ubuntu/Debian)
sudo apt-get install redis-server

# Iniciar Redis
sudo systemctl start redis-server
```

### 3. Configure o Eureka Server
Certifique-se de que o Eureka Server está rodando na porta 8761.

### 4. Execute a aplicação
```bash
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 🔧 Configuração

### Rotas Configuradas

| Rota | Serviço de Destino | Descrição |
|------|-------------------|-----------|
| `/api/users/**` | user-service | Gerenciamento de usuários |
| `/api/products/**` | product-service | Catálogo de produtos |
| `/api/orders/**` | order-service | Processamento de pedidos |
| `/api/auth/**` | auth-service | Autenticação e autorização |

### Rate Limiting

- **User Service**: 10 req/s por usuário, burst de 20
- **Product Service**: 20 req/s por IP, burst de 40
- **Order Service**: Sem rate limiting (requer autenticação)

### Circuit Breaker

Configurado para todos os serviços com:
- **Failure Rate Threshold**: 50%
- **Wait Duration**: 30s
- **Sliding Window**: 10 requests
- **Minimum Calls**: 5

## 🔐 Autenticação

### Endpoints Públicos (sem autenticação)
- `/api/auth/login`
- `/api/auth/register`
- `/api/auth/refresh`
- `/health/**`
- `/actuator/**`

### Endpoints Protegidos
Todos os outros endpoints requerem um token JWT válido no header:
```
Authorization: Bearer <jwt-token>
```

## 📊 Monitoramento

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Métricas Prometheus
```bash
curl http://localhost:8080/actuator/prometheus
```

### Gateway Routes
```bash
curl http://localhost:8080/actuator/gateway/routes
```

## 🧪 Testando a API

### 1. Teste de Health Check
```bash
curl -X GET http://localhost:8080/health
```

### 2. Teste de Rate Limiting
```bash
# Fazer múltiplas requisições para testar o rate limiting
for i in {1..15}; do
  curl -X GET http://localhost:8080/api/products/test
  echo "Request $i"
done
```

### 3. Teste de Circuit Breaker
```bash
# Simular falha do serviço para ativar o circuit breaker
curl -X GET http://localhost:8080/api/users/test
```

## 🔧 Configurações Avançadas

### Personalizar Rate Limiting
Edite o arquivo `application.yml`:
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: custom-service
          uri: lb://custom-service
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 50
                redis-rate-limiter.burstCapacity: 100
```

### Adicionar Novos Filtros
Crie uma nova classe que estenda `AbstractGatewayFilterFactory`:
```java
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    // Implementação do filtro
}
```

## 🐛 Troubleshooting

### Problemas Comuns

1. **Erro de Conexão com Redis**
   - Verifique se o Redis está rodando
   - Confirme a configuração de host/porta

2. **Serviços não encontrados**
   - Verifique se o Eureka Server está rodando
   - Confirme se os microserviços estão registrados

3. **Erro de Autenticação**
   - Verifique se o token JWT é válido
   - Confirme a configuração do JWT secret

## 📝 Logs

Os logs são configurados para mostrar:
- Requisições e respostas detalhadas
- Informações de autenticação
- Métricas do Gateway
- Erros e exceções

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📞 Suporte

Para suporte, entre em contato através de:
- Email: suporte@exemplo.com
- Issues: [GitHub Issues](https://github.com/seu-usuario/api-gateway-springboot/issues)

---

**Desenvolvido com ❤️ usando Spring Cloud Gateway**
