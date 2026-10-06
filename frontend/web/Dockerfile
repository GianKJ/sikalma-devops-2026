# ---------------------------------------------------------
# Dockerfile para Frontend React (Vite) - Laboratorio DevOps
# Cumple con el contrato obligatorio: ARG PORT, ENV PORT, EXPOSE
# ---------------------------------------------------------

# Stage 1: Build de la aplicacion React
FROM node:20-alpine AS builder
WORKDIR /app

COPY package.json package-lock.json* ./
RUN npm ci || npm install

COPY . .
RUN npm run build

# Stage 2: Servidor estatico liviano
FROM node:20-alpine
WORKDIR /app

ARG PORT=4200
ENV PORT=${PORT}

# Instalar serve para entregar los estaticos en el puerto inyectado
RUN npm install -g serve

COPY --from=builder /app/dist ./dist

# Usuario no-root para seguridad
USER node

EXPOSE ${PORT}

CMD ["sh", "-c", "serve -s dist -l ${PORT}"]
