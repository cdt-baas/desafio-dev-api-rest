FROM node:alpine

RUN mkdir /src

WORKDIR /src

COPY ["package.json", "package-lock.json", "./"]

RUN npm install

COPY ./app .

EXPOSE 3000

CMD npm run dev