import "reflect-metadata";
import path from "node:path";
import { DataSource } from "typeorm";

export const AppDataSource = new DataSource({
  type: "postgres",
  url: process.env.DATABASE_URL,
  entities: [path.join(__dirname, "./entities/*.{ts,js}")],
  synchronize: process.env.ENV !== "PROD",
});
