import "dotenv/config";
import { AppDataSource } from "@infra/database/data-source";
import app from "@interfaces/http/http";
import { logger } from "@infra/database/logger";

async function start() {
  await AppDataSource.initialize();
  logger.info("Database connected successfully");
  app.listen(process.env.PORT, () => {
    logger.info(`Server is running on port ${process.env.PORT}`);
  });
}
start();
