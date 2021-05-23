FROM node:15
WORKDIR /app
COPY package*.json ./
RUN npm install --production
COPY . .

EXPOSE 3434
CMD ["node", "src/index.js"]