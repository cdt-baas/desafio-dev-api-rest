import winston from "winston";

export const logger = winston.createLogger({
  transports: [
    new winston.transports.Console({
      format: winston.format.combine(
        winston.format.ms(),
        winston.format.timestamp(),
        winston.format.json(),
        winston.format.colorize({ all: true }),
      ),
    }),
  ],
});
