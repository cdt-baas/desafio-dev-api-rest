export {};

declare global {
  namespace NodeJS {
    interface ProcessEnv {
      [key: string]: string | undefined;
      DATABASE_URL: string;
      PORT: string;
      ENV: string;
    }
  }
}
