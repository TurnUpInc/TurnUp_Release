FROM node:12-alpine AS BUILD_IMAGE

WORKDIR /UserManager/

COPY package.json .

RUN npm install --production

COPY . .