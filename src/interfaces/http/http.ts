import express from "express";
import swaggerUi from "swagger-ui-express";
import swaggerJSDoc from "swagger-jsdoc";
import path from "node:path";
import { rateLimit } from "express-rate-limit";
import holdersRoutes from "./routes/holdersRouter";
import accoutsRoutes from "./routes/accountsRouter";
import healthRouter from "./routes/healthRouter";
import { errorHandlerMiddleware } from "./middlewares/errorHandlerMiddleware";
import { swaggerDefinition } from "./swagger";

const app = express();

const options = {
  swaggerDefinition,
  apis: [path.join(__dirname, "./routes/*.{ts,js}")],
};

const swaggerSpec = swaggerJSDoc(options);

const limiter = rateLimit({
  windowMs: 1 * 60 * 1000, //1 minute
  max: 1, //max request
  handler: (req, res) => {
    res.status(429).json({
      type: "TooManyRequests",
      message:
        "Você atingiu o limite de requisições, aguarde um pouco e tente novamente.",
    });
  },
});

app.use(express.json());
app.use(limiter);
app.use("/api/health", healthRouter);
app.use("/api/holders", holdersRoutes);
app.use("/api/accounts", accoutsRoutes);
app.use("/api/docs", swaggerUi.serve, swaggerUi.setup(swaggerSpec));

app.use(errorHandlerMiddleware);

export default app;
