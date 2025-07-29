cl{{- define "config-server.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "config-server.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}

{{- define "config-server.selectorLabels" -}}
app: {{ include "config-server.name" . }}
{{- end }}

{{- define "config-server.labels" -}}
app: {{ include "config-server.name" . }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
release: {{ .Release.Name }}
heritage: {{ .Release.Service }}
{{- end }}