package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

func main() {
	r := gin.Default()

	r.GET("/v1/product", func(c *gin.Context) {
		c.String(http.StatusOK, "product!")
	})

	r.GET("/healthcheck", func(c *gin.Context) {
		c.String(http.StatusOK, "ok")
	})

	r.Run()
}
