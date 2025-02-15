 **ODS 12 - Consumo e Produção Responsáveis**, promovendo uma gestão sustentável dos resíduos. 
---

## 🏗 **Plano de Desenvolvimento Integrado**
### 🎯 **Objetivo do Projeto**
Criar um **Sistema de Monitoramento de Resíduos** onde moradores possam registrar seus descartes, visualizar estatísticas sobre o lixo coletado e obter previsões sobre a geração futura de resíduos, incentivando a reciclagem e a redução do desperdício.

---

## 🔗 **Integração das Disciplinas**

| 📚 Disciplina | 📌 Aplicação no Projeto |
|--------------|------------------------|
| **Inteligência Artificial** 🤖 | Utilizar **Machine Learning (ML)** para prever a quantidade futura de resíduos descartados com base em dados históricos. |
| **Engenharia de Software Baseada em Componentes** 🏗 | Modularizar o sistema em **componentes reutilizáveis** (ex: serviços independentes para autenticação, previsão de resíduos, cadastro de coleta). |
| **Computação em Nuvem** ☁ | Hospedar a API no **Heroku** com **PostgreSQL** e utilizar um serviço de armazenamento para backups dos dados. |
| **Arquiteturas e Padrões de Software** 🏛 | Implementar **MVC (Model-View-Controller)** no backend com Spring Boot, além de padrões como **Repository, DTO e Service Layer**. |
| **Projeto de Software com Métodos Ágeis** 🚀 | Usar **Scrum** para gerenciar o desenvolvimento (ex: definir backlog, sprints e daily meetings). Ferramenta sugerida: **Trello**. |
| **Redes de Computadores** 🔐 | Implementar **HTTPS, autenticação JWT e hash de senhas** para proteger os dados dos usuários. |

---

## 🏛 **Arquitetura do Sistema**
**Backend**:  
📌 **Spring Boot** (REST API, Thymeleaf, JPA, PostgreSQL)  
📌 **HTMX** (Front dinâmico sem necessidade de React)  
📌 **Python + Scikit-learn** (para previsão de resíduos)

**Frontend**:  
📌 **HTMX + Thymeleaf** (interface dinâmica sem necessidade de SPA)

**Banco de Dados**:  
📌 **PostgreSQL** (armazenamento dos dados de resíduos)

**Hospedagem**:  
📌 **Heroku** (backend)  

---

## 🔥 **Funcionalidades Planejadas**
✅ **Admin cadastra pontos de coleta**  
✅ **Moradores registram resíduos descartados**  
✅ **Dashboard com gráficos de resíduos coletados**  
✅ **Previsão de geração de resíduos usando IA**  
✅ **Exportação de relatórios (PDF/Excel)**  
✅ **Sistema seguro com autenticação JWT**

---

## 🔍 **Exemplo de Aplicação da Inteligência Artificial**
### **1️⃣ Coletar dados históricos**
- Data da coleta
- Tipo de resíduo (plástico, papel, vidro, etc.)
- Quantidade (kg)

### **2️⃣ Aplicar um modelo preditivo**
- Treinar um modelo **Regressão Linear** ou **ARIMA** (para séries temporais) com os dados históricos.
- Implementar um **serviço Python** para processar as previsões e retornar dados para o sistema.

📌 **Exemplo de código para previsão (Python + Scikit-Learn)**:
```python
import pandas as pd
from sklearn.linear_model import LinearRegression

# Dados simulados (data, quantidade de resíduos)
dados = {'dias': [1, 2, 3, 4, 5, 6, 7], 'residuos': [10, 12, 15, 18, 20, 25, 30]}
df = pd.DataFrame(dados)

# Treinamento do modelo
modelo = LinearRegression()
modelo.fit(df[['dias']], df['residuos'])

# Previsão para os próximos 3 dias
previsao = modelo.predict([[8], [9], [10]])
print("Previsão de resíduos para os próximos dias:", previsao)
```

No **Spring Boot**, esse modelo pode ser consumido via **API REST** usando **FastAPI** ou Flask.




📌



## 🚀 **Conclusão**
O projeto integra **todas as disciplinas** do semestre de forma prática e alinhada com a **ODS 12**:

✅ **ODS 12 - Consumo e Produção Sustentáveis**  
📌 Monitoramento e previsão de resíduos incentivam a reciclagem e a redução do desperdício.

✅ **Inteligência Artificial**  
📌 Previsão de geração de resíduos usando Machine Learning.

✅ **Engenharia de Software Baseada em Componentes**  
📌 Backend modularizado (Camadas MVC, Repository, DTO, etc.).

✅ **Computação em Nuvem**  
📌 Hospedagem no Heroku com PostgreSQL.

✅ **Arquiteturas e Padrões de Software**  
📌 Utilização de padrões como MVC, Repository e Service Layer.

✅ **Projeto de Software com Métodos Ágeis**  
📌 Metodologia Scrum com backlog e sprints.

✅ **Redes de Computadores**  
📌 Segurança com HTTPS e autenticação JWT.

