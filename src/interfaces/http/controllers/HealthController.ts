import { Request, Response } from "express";
import { AppDataSource } from "@infra/database/data-source";

export class HealthController {
  async check(req: Request, res: Response) {
    const dbStatus = await this.checkDatabase();
    const statusCode = dbStatus === "connected" ? 200 : 500;
    res.status(statusCode).json({
      app: "ok",
      database: dbStatus,
      uptime: process.uptime(),
      timestamp: new Date().toISOString(),
    });
  }

  private async checkDatabase(): Promise<"connected" | "disconnected"> {
    try {
      await AppDataSource.query("SELECT 1");
      return "connected";
    } catch (error) {
      console.error("Database connection error:", error);
      return "disconnected";
    }
  }
}
