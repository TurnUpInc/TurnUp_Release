FROM node:12-alpine AS BUILD_IMAGE

WORKDIR /LocationManager/

COPY package.json .

RUN npm install --production

COPY . .