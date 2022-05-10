FROM node:14-alpine as vue-build
COPY . /app/
WORKDIR /app
RUN npm install
RUN npm run-script build

FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=vue-build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]