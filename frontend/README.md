# Frontend

Frontend de SAC construido con Angular 21 y componentes standalone.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`.

## Authentication flow

- The app uses `POST /api/v1/usuarios/login` from the backend.
- The login view lives at `/login`.
- After a successful login the user is redirected to `/dashboard`.
- The JWT session is stored in `localStorage` under the key `sac.sesion`.
- The backend base URL is defined in `src/app/core/api.config.ts`.

## Running unit tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
