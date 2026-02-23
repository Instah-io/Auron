package io.instah.auron.gradlePlugin.web

import io.instah.auron.gradlePlugin.config.AuronConfigScope

fun generateScriptTags(config: AuronConfigScope) = config.webScripts.joinToString(separator = "\n\n") { script ->
    """
        <script>
        $script
        </script>
    """.trimIndent()
}

//TODO: Add support for tags like og:title and og:description
fun generateIndexHtml(
    config: AuronConfigScope
): String = """
    <!DOCTYPE html>
    <html>
        <head>
            <script src="${config.configUUID}-main.js"></script>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            
            ${generateScriptTags(config)}
            
            <style>
                #ComposeViewport {
                    position: absolute;
                    height: 100%;
                    width: 100%;
                }

                body {
                    margin: 0;
                }
            </style>
        </head>
        
        <body>
            <div id="ComposeViewport"></div>
        </body>
    </html>
""".trimIndent()